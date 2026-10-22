pipeline {

    agent any

    environment {
        KEYSTORE_FILE = credentials('KEYSTORE_FILE')
        KEYSTORE_PASSWORD = credentials('KEYSTORE_PASSWORD')
        KEY_ALIAS = credentials('KEY_ALIAS')
        KEY_PASSWORD = credentials('KEY_PASSWORD')
		//NOTIFICATION_EMAILS = credentials('NOTIFICATION_EMAILS') viene usato direttamente in sendNotification; in locale non serve
        //GOOGLE_CLOUD_IMGROLLER_JSON viene usato direttamente nello stage('Deploy to Play Store'); in locale non serve perchè sono io in quel caso a caricare a mano il file sul PS
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Repository scaricato con successo!'
            }
        }

        stage('Environment') {
            steps {
                sh 'whoami'
                sh 'java -version'
                sh 'git --version'
				sh 'echo $ANDROID_HOME'
				sh 'echo $ANDROID_SDK_ROOT'
				sh 'sdkmanager --version'
				sh 'adb version'
            }
        }

        stage('Project inspection') {
            steps {
                sh 'pwd'
                sh 'ls -la'
            }
        }
		
		stage('Prepare Android') {
			steps {
				sh '''
					cat > local.properties <<EOF
					sdk.dir=$ANDROID_SDK_ROOT
					EOF

					echo "local.properties creato:"
					cat local.properties

					ls -la
				'''
			}
		}

         stage('Gradle Version') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew --version'
            }
        }

        stage('Unit Tests') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Lint') {
            steps {
                sh './gradlew lint'
            }
        }

        stage('Bundle Release') {
            steps {
                sh './gradlew bundleRelease'
            }
        }

        stage('Inspect AAB') {
            steps {
                sh '''
                    echo "=== AAB trovati ==="
                    find . -name "*.aab"
                    ls -l app/build/outputs/bundle/release/
                '''
            }
        }

		stage('Validate Release Notes') {
			steps {
				script {
					def releaseNotesFile = 'release_notes.md'

					// Controlla che il file esista
					if (!fileExists(releaseNotesFile)) {
						error("Release notes file not found: ${releaseNotesFile}")
					}

					// Legge il contenuto
					def releaseNotes = readFile(releaseNotesFile).trim()

					// Controlla che non sia vuoto
					if (releaseNotes.isEmpty()) {
						error("Release notes file is empty")
					}

					// Controlla la lunghezza
					if (releaseNotes.length() > 500) {
						error(
							"Release notes are too long: " +
							"${releaseNotes.length()} characters (maximum 500)"
						)
					}

					// Salva il contenuto per lo stage successivo
					env.RELEASE_NOTES = releaseNotes

					echo "=== Release Notes validated ==="
					echo "Characters: ${releaseNotes.length()}"
					echo "Content:"
					echo releaseNotes
				}
			}
		}

		stage('Deploy to Play Store') {
			steps {
				echo '=== Deploy su Internal testing ==='

				androidApkUpload(
					googleCredentialsId: 'GOOGLE_CLOUD_IMGROLLER_JSON',
					apkFilesPattern: 'app/build/outputs/bundle/release/app-release.aab',
					trackName: 'internal',
					rolloutPercentage: '100',
					recentChangeList: [
						[
							language: 'en',
							text: env.RELEASE_NOTES
						]
					]
				)

				echo '=== Promozione a Closed testing ==='

                androidApkMove(
                    googleCredentialsId: 'GOOGLE_CLOUD_IMGROLLER_JSON',
                    apkFilesPattern: 'app/build/outputs/bundle/release/app-release.aab',
                    fromVersionCode: false,
                    trackName: 'alpha',
                    rolloutPercentage: '100'
                )
			}
		}
    }
	
	post {
        success {
            sendNotification(true)
        }
        failure {
            sendNotification(false)
        }
    }
}

def sendNotification(Boolean Successo) {
    if (env.NOTIFICATION_EMAILS && env.NOTIFICATION_EMAILS.trim()) {
        // Prepara la stringa con il tempo di compilazione
        def duration = currentBuild.durationString.replace("and counting", "").trim()

        if (Successo) {
            echo "Invio Email di successo a ${env.NOTIFICATION_EMAILS}"
            emailext(
                subject: "✅ Build riuscita - ${env.JOB_NAME}",
                body: "La build ${env.BUILD_NUMBER} è terminata con successo.\nDurata: ${duration}",
                to: env.NOTIFICATION_EMAILS
            )
        } else {
            echo "Invio Email di fallimento a ${env.NOTIFICATION_EMAILS}"
            emailext(
                subject: "❌ Build fallita - ${env.JOB_NAME}",
                body: "La build ${env.BUILD_NUMBER} è fallita.\nDurata: ${duration}",
                to: env.NOTIFICATION_EMAILS,
                attachLog: true
            )
        }
    } else {
        echo "Email non inviata: NOTIFICATION_EMAILS non è configurata o è vuota."
    }
}
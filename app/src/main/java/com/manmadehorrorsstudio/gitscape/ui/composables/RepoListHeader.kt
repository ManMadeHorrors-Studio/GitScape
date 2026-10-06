package com.manmadehorrorsstudio.gitscape.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.manmadehorrorsstudio.gitscape.R
import com.manmadehorrorsstudio.gitscape.TITLES_SIZE
import com.manmadehorrorsstudio.gitscape.domain.models.GitHubUser
import com.manmadehorrorsstudio.gitscape.ui.managers.localizedString
import com.manmadehorrorsstudio.gitscape.ui.theme.*

@Composable
fun RepoListHeader(
    gitHubUser: GitHubUser?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            var avatarSize = 0.dp
            gitHubUser?.avatarUrl?.let { au ->
                avatarSize = 60.dp
                Image(
                    painter = rememberAsyncImagePainter(au),
                    contentDescription = gitHubUser.userName,
                    modifier = Modifier
                        .size(avatarSize)
                        .align(Alignment.CenterStart)
                        .clip(CircleShape)
                )

                Spacer(
                    modifier = Modifier.width(10.dp)
                )
            }

            Text(
                text = gitHubUser?.userName ?: localizedString(R.string.app_name),
                fontSize = TITLES_SIZE,
                fontWeight = FontWeight.Bold,
                color = LightAzure,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = avatarSize + 5.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RepoListHeaderPreview() {
    Surface {
        RepoListHeader(
            gitHubUser = GitHubUser(
                id = "666",
                userName = "Man Made Horrors Studio",
                avatarUrl = "https://avatars.githubusercontent.com/u/263767797?v=4"
            )
        )
    }
}
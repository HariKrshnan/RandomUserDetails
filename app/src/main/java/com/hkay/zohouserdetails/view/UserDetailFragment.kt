package com.hkay.zohouserdetails.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.compose.rememberImagePainter
import com.hkay.zohouserdetails.R
import com.hkay.zohouserdetails.databinding.FragmentUserDetailBinding

class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {
    private var userDetailBinding: FragmentUserDetailBinding? = null
    private val binding get() = userDetailBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userDetailBinding = FragmentUserDetailBinding.inflate(inflater, container, false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CardLayout()
            }
        }
        return binding.root
    }

    @Preview
    @Composable
    fun CardLayout() {
        return MaterialTheme {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical
                    )
            ) {
                Card(
                    elevation = 12.dp,
                    backgroundColor = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(0.dp, 0.dp, 14.dp, 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = rememberImagePainter(arguments?.getString("pictureUrl")),
                            contentDescription = null,
                            modifier = Modifier.size(228.dp)
                        )
                        arguments?.getString("userName")?.let {
                            Text(
                                text = it,
                                modifier = Modifier.padding(6.dp),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
                Text(
                    text = getString(R.string.user_description),
                    modifier = Modifier.padding(12.dp, 12.dp, 12.dp, 100.dp)
                )

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        userDetailBinding = null
    }
}
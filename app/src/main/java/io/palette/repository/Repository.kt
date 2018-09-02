package io.palette.repository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
        val pickRepository: PickRepository,
        val unsplashRepository: UnsplashRepository,
        val detailRepository: DetailRepository,
        val profileRepository: ProfileRepository
)
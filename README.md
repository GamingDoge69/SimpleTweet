# Project 2 - Simple Tweet

Simple Tweet is an android app that allows a userData to view their Twitter timeline. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **15** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **sign in to Twitter** using OAuth login
- [x]	User can **view tweetData from their home timeline**
  - [x] User is displayed the username, name, and body for each tweetData
  - [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweetData "8m", "7h"
- [x] User can refresh tweetData timeline by pulling down to refresh

The following **optional** features are implemented:

- [x] User can view more tweetData as they scroll with infinite pagination
- [x] Improve the userData interface and theme the app to feel "twitter branded"
  - Will do more next week. Prio on features this week.
- [x] Links in tweetData are clickable and will launch the web browser
- [x] User can tap a tweetData to display a "detailed" view of that tweetData
- [x] User can see embedded image mediaData within the tweetData detail view
- [x] User can watch embedded video within the tweetData
- [ ] User can open the twitter app offline and see last loaded tweetData
- [x] On the Twitter timeline, leverage the CoordinatorLayout to apply scrolling behavior that hides / shows the toolbar.

## Video Walkthrough

Here's a walkthrough of implemented userData stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

- EndlessRecyclerViewScrollListener was VERY BUGGY (my mistake)
   - For some reason RecyclerView forget the position of its children.
   - I've made a mistake in my notifyItemRangeInserted calls.
   - After performance modifications and corrections, performance was extremely fluid.
- My solution for Media is very basic and doesn't scale. Will consider rewritting in part 2.
   - A MediaFactory of some sort is better.
- VideoView's Struggle with Twitter's MP4 gifs
   - Used ExoPlayer a more modern VideoPlayer
   - ExoPlayer are not lifecycler-aware but it's not too hard to overcome that
   - Styling of ExoPlayer is less than ideal, considering styling in part 2.
- **Twitter API Limits Are Awful and Slow Development Process**


## Open-source libraries used

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android
- [ExoPlayer](https://github.com/google/ExoPlayer) - Application level mediaData player for Android

## License

    Copyright 2022 John Santelises

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

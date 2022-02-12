# Project 2 - Simple Tweet

Simple Tweet is an android app that allows a user to view their Twitter timeline. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **15** hours spent in total

## User Stories

The following **required** functionality is completed:

- [x] User can **sign in to Twitter** using OAuth login
- [x]	User can **view tweets from their home timeline**
  - [x] User is displayed the username, name, and body for each tweet
  - [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
- [x] User can refresh tweets timeline by pulling down to refresh

The following **optional** features are implemented:

- [x] User can view more tweets as they scroll with infinite pagination
- [x] Improve the user interface and theme the app to feel "twitter branded"
  - Will do more next week. Prio on features this week.
- [x] Links in tweets are clickable and will launch the web browser
- [x] User can tap a tweet to display a "detailed" view of that tweet
- [x] User can see embedded image media within the tweet detail view
- [x] User can watch embedded video within the tweet
- [ ] User can open the twitter app offline and see last loaded tweets
- [x] On the Twitter timeline, leverage the CoordinatorLayout to apply scrolling behavior that hides / shows the toolbar.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://user-images.githubusercontent.com/43687971/153701564-bd028615-ff18-4b9b-be6c-e5a1923a79de.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

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
- [ExoPlayer](https://github.com/google/ExoPlayer) - Application level media player for Android

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

# Project 2 - Simple Tweet

**Simple Tweet** is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **10** hours spent in total

## User Stories Part 2

The following **required** functionality is completed:

- [x] User can **compose and post a new tweet**
  - [x] ~~User can click a “Compose” icon in the Action Bar on the top right~~
      Doing FAB Stretch Story
  - [x] User can then enter a new tweet and post this to twitter
  - [x] User is taken back to home timeline with **new tweet visible** in timeline
  - [x] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh
  - [x] User can **see a counter with total number of characters left for tweet** on compose tweet page

The following **optional** features are implemented:

- [x] User is using **"Twitter branded" colors and styles**
- [x] ~~User can click links in tweets launch the web browser~~ Did in Part 1
- [x] User can **select "reply" from detail view to respond to a tweet**
  - Accessible from Timeline as well.
- [x] The "Compose" action is moved to a FloatingActionButton instead of on the AppBar
- [x] Compose tweet functionality is build using modal overlay
- [x] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.org/android/Using-Parceler).
- [ ] User can **open the twitter app offline and see last loaded tweets**. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.
- [ ] When a user leaves the compose view without publishing and there is existing text, prompt to save or delete the draft. If saved, the draft should then be **persisted to disk** and can later be resumed from the compose view.
- [ ] Enable your app to receive implicit intents from other apps. When a link is shared from a web browser, it should pre-fill the text and title of the web page when composing a tweet.

The following **additional** features are implemented:

- [x] User can like and retweet tweets.
- [x] User can see Like/Retweet Counts
 
## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## User Stories Part 1
Time spent: **15** hours spent in total

The following **required** functionality is completed:

- [x] User can **sign in to Twitter** using OAuth login
- [x]	User can **view tweetData from their home timeline**
  - [x] User is displayed the username, name, and body for each tweetData
  - [x] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweetData "8m", "7h"
- [x] User can refresh tweetData timeline by pulling down to refresh

The following **optional** features are implemented:

- [x] User can view more tweet as they scroll with infinite pagination
- [x] Improve the user interface and theme the app to feel "twitter branded"
- [x] Links in tweetData are clickable and will launch the web browser
- [x] User can tap a tweetData to display a "detailed" view of that tweetData
- [x] User can see embedded image mediaData within the tweetData detail view
- [x] User can watch embedded video within the tweetData
- [ ] User can open the twitter app offline and see last loaded tweetData
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
   - ExoPlayer are not lifecycle-aware but it's not too hard to overcome that
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

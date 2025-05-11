# Forum Deal Notifier Core

#### A library for reading a continuous stream of posts (such as RSS) and notifying users of ones that match their interests

## Description

This is meant to be a way to alert a user of posts that match queries that can be set.

Here is a screenshot from a a simple Telegram bot built using this library:

![image](https://github.com/user-attachments/assets/59c0181c-0f81-4b4f-a665-1bbb461427c8)

The telegram bot is not ready for general release and is proprietary, if you want access to it feel free to email me at michaeldombrovsky@gmail.com

## Limitations

This can only work on sequential RSS/other post feeds, as it uses the published date to keep track of already shown
posts.

**If the published date is missing or not sorted, such as a RSS feed for popular posts, this will not work**

## Usage:
```kt
val manager = PostFinderManager()

// This uses RedFlagDeals new posts feed to alert you of new deals
val scraper: Scraper = RFDNewScraper()

val user = User("John")

// MatchAll is a simple query that matches all posts, mostly used for debugging
user.queriesManager.addQuery(
    query = MatchAll(),
    postFinder = manager.getPostFinder(scraper),
    notifier = PrintlnNotifier(),
    user = user
)

// This will reload the feed for new posts every 60 seconds
manager.startPollingForNewPosts(60)
```


## Examples

Examples of how to use this can be found in the [examples](src/main/kotlin/examples) folder

## Q&A

### Don't like how the queries work?

I don't blame you they were created years ago for the purpose of being easy to visualize, not parse text well.

Want something more useful like regex support? No worries, you can just implement the [Query](src/main/kotlin/query/Query.kt) interface and create it.

### Want more notification options?

Implement the [Notifier](src/main/kotlin/notification/Notifier.kt) and [NotifierCredentials](src/main/kotlin/notification/NotifierCredentials.kt) classes, with your favourite notification service, such as Discord.

### Your favourite website does not support RSS, or supports it badly?

Just implement the Scraper interface, a good example of that is the [RedditJSONScraper](src/main/kotlin/scraper/custom/RedditJSONScraper.kt)

## Contribution

Contribution is always welcome, feel free to create a pull request

Note: Most of this code was taken from a previous app project that was written years
ago: https://github.com/mdombrovsky/deals-notifier

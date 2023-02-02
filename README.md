# RSS Sniper library

#### A library for reading a continuous stream of posts (such as RSS) and notifying users of ones that match their interests

## Description WIP

This is meant of a way to alert a user of posts that match queries that can be set.

Users can be notified of alerts that are triggered through:

- Telegram

## Limitations

This can only work on sequential RSS/other post feeds, as it uses the published date to keep track of already shown posts.

**If the published date is missing or not sorted, such as a RSS feed for popular posts, this will not work**

## Usage: WIP

## Examples

Examples of how to use this can be found in the /src/main/kotlin/examples folder

## Q&A

### Don't like how the queries work?

I don't blame you they were created years ago for the purpose of being easy to visualize, not parse text well.

Want something more useful like regex support? No worries, you can just implement the Query interface and create it.

### Want more notification options?

Implement the Notifier and NotifierCredentials classes, with your favourite notification service, such as Discord.

### Your favourite website does not support RSS, or supports it badly?

Just implement the Scraper interface, a good example of that is the RedditJSONScraper

## Contribution

Contribution is always welcome, feel free to create a pull request

Note: Most of this code was taken from a previous app project that was written years
ago: https://github.com/mdombrovsky/deals-notifier

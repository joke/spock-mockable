# Changelog

## [2.2.1](https://github.com/joke/spock-mockable/compare/v2.2.0...v2.2.1) (2022-11-22)


### Bug Fixes

* no stacktrace on redefinition error ([a57195c](https://github.com/joke/spock-mockable/commit/a57195c1a18fb67864b6bc6e57092aca62e3bffb))

## [2.2.0](https://github.com/joke/spock-mockable/compare/v2.1.1...v2.2.0) (2022-11-13)


### Features

* improve lazy startup ([e77be55](https://github.com/joke/spock-mockable/commit/e77be55c78706ab5425b048277594d865c702c23))
* support mocking static methods ([52f76ac](https://github.com/joke/spock-mockable/commit/52f76ac409a5d25b268c8a8e653567db62020ed8))

## [2.1.1](https://github.com/joke/spock-mockable/compare/v2.1.0...v2.1.1) (2022-11-07)


### Bug Fixes

* agent deactivation via system property ([db901d5](https://github.com/joke/spock-mockable/commit/db901d543a76ed97eba44838496418afdcab80af))
* publishing shadow artifact closes [#269](https://github.com/joke/spock-mockable/issues/269) ([25eb3b1](https://github.com/joke/spock-mockable/commit/25eb3b14457255fcbfb9bf2042b15a2ca16afd8a))

## [2.1.0](https://github.com/joke/spock-mockable/compare/v2.0.0...v2.1.0) (2022-11-06)


### Features

* support earlier transformation as -javaagent ([84b4fd5](https://github.com/joke/spock-mockable/commit/84b4fd5655fec27a90fda21a9709659054958abe))

## [2.0.0](https://github.com/joke/spock-mockable/compare/v1.5.7...v2.0.0) (2022-11-05)


### ⚠ BREAKING CHANGES

* `@Mockable` changed in favor of automatic detection.
* drop support for spock 1.3

### Features

* automatic detected of mockable classes ([fb03556](https://github.com/joke/spock-mockable/commit/fb03556e3960ba307ee54261c8eb02f00c2df381))


### Code Refactoring

* drop support for spock 1.3 ([96753f6](https://github.com/joke/spock-mockable/commit/96753f695ff8d2630aac02cd130cc0c6165e38b0))

## [1.5.7](https://github.com/joke/spock-mockable/compare/v1.5.6...v1.5.7) (2022-09-24)


### Bug Fixes

* spock 2.2 update ([6a6a92d](https://github.com/joke/spock-mockable/commit/6a6a92dae1b4f77b3b50ef74fe5994fa44d1caad))

## [1.5.6](https://github.com/joke/spock-mockable/compare/v1.5.5...v1.5.6) (2022-08-16)


### Bug Fixes

* compatibility with Byte-Buddy 1.12.13+. closes [#208](https://github.com/joke/spock-mockable/issues/208) ([5590b59](https://github.com/joke/spock-mockable/commit/5590b5993bcc30ef20b7d64b9573527d10b15a31))

## [1.5.5](https://github.com/joke/spock-mockable/compare/v1.5.4...v1.5.5) (2022-06-06)


### Bug Fixes

* earlier junit startup if possible ([7e8d519](https://github.com/joke/spock-mockable/commit/7e8d5197c1d7c8942792bae466d87cd967896184))

## [1.5.4](https://github.com/joke/spock-mockable/compare/v1.5.3...v1.5.4) (2022-06-05)


### Bug Fixes

* improve debug logging ([7000c03](https://github.com/joke/spock-mockable/commit/7000c0353f9013a8037ccf0ece82a74ea7b49eb9))

### [1.5.3](https://github.com/joke/spock-mockable/compare/v1.5.2...v1.5.3) (2022-05-01)


### Bug Fixes

* spock 2.2 support ([607a2b3](https://github.com/joke/spock-mockable/commit/607a2b3f183f640d6d430d332815580e006f6b10))

### [1.5.2](https://github.com/joke/spock-mockable/compare/v1.5.1...v1.5.2) (2022-05-01)


### Bug Fixes

* exception during processing of classes with unknown type ([f9c5d64](https://github.com/joke/spock-mockable/commit/f9c5d64e17d4a008c02cd14c9335526c204b032c))

### [1.5.1](https://github.com/joke/spock-mockable/compare/v1.5.0...v1.5.1) (2022-02-10)


### Bug Fixes

* fixing release publishing ([3b00289](https://github.com/joke/spock-mockable/commit/3b002892c52da583d69b28cb8f896bf70ce23312))

## [1.5.0](https://github.com/joke/spock-mockable/compare/v1.4.4...v1.5.0) (2022-02-07)


### Features

* Make complete packages mockable ([0c17d8d](https://github.com/joke/spock-mockable/commit/0c17d8d8c9f27378b2d2ec98f9f8f4bc8f717f98))

### [1.4.4](https://www.github.com/joke/spock-mockable/compare/v1.4.3...v1.4.4) (2021-11-26)


### Bug Fixes

* support for spock1.3-groovy2.4. fixes [#112](https://www.github.com/joke/spock-mockable/issues/112) ([cb01ead](https://www.github.com/joke/spock-mockable/commit/cb01ead6c913b333bae807bd5f576f9972710db3))

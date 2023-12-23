## [1.1.1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/compare/1.1.0...1.1.1) (2023-12-23)


### Dependency updates

* **deps:** update plugin java-qa to v1.31.0 ([95743fd](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/95743fd5c8ae5e3c6b97f98e753847b762c347c0))


### Documentation

* add rest api documentation ([0a4a2ee](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/0a4a2ee37fc318238bace97cbc56ac001a45d966))


### Tests

* add tests on dtkg engine action part ([a4c2b8b](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/a4c2b8b158730ee79dd1e7a4ce37a8d750136328))


### Build and continuous integration

* configure ci to deploy rest api documentation ([1349a35](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/1349a351cf6d825c7a6d4e9c8568445cb03dee57))


### Style improvements

* remove useless logging ([633b969](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/633b969a3e9d4f5623c603cae8a6421826312021))
* rename variable to be more explicit ([a1afdcc](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/a1afdcc0652d68d8ce1c09577b6749685320b356))

## [1.1.0](https://github.com/WebBased-WoDT/wldt-wodt-adapter/compare/1.0.1...1.1.0) (2023-12-20)


### Features

* include the availability of actions within the Digital Twin Knowledge Graph ([114735e](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/114735ed276c941885c9deb9c763bc1b4378ad01))


### Dependency updates

* **deps:** update dependency org.apache.jena:apache-jena-libs to v4.10.0 ([58c7a87](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/58c7a874bdd9ab48f2c987c8d50f05ec9171700e))


### Bug Fixes

* fix string serialization of wodt vocabulary ([47d178f](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/47d178fef11c9e9eca4a2914eea0ef31b159af63))


### Tests

* adapt test file formatting to new apache jena version ([096f91d](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/096f91d8fec331baf355f10a8490fea0306ce0e1))


### General maintenance

* add available action id predicate to vocabulary ([f1f6f3f](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/f1f6f3f09d36f60ef245aa05385a1f9222d43e7a))

## [1.0.1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/compare/1.0.0...1.0.1) (2023-12-19)


### Bug Fixes

* add to the dtkg also the initial value of a property ([08485a1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/08485a1821fb9c63854970fc7c1e659641d12f58))


### General maintenance

* avoid useless continuous publishing ([dd6e9fc](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/dd6e9fc81380c59fa682583eecfe241bb22d49ca))

## 1.0.0 (2023-12-19)


### Features

* add dtkg to wldt wodt digital adapter ([ff07434](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/ff07434da7bd45ca2f647033bfc145283dbe1436))
* handle the deletion of a property in the dtkg engine ([02c0ab1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/02c0ab19d195b25599ce0bf916a772a9d5c927d2))
* handle the deletion of a property in the wodt digital adapter ([01ec5b9](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/01ec5b9a69f11af7ed7f72006a41b817e7b51367))
* implement dtd manager ([e0c808a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/e0c808a0b76a1bcd052645e478f9112f38bce9a4))
* implement first version of the dtkg engine ([b626522](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/b62652215145c5e4c58668a61b379cae69733167))
* implement first version of the platform management interface ([21d2dfb](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/21d2dfbd2f0fb427bb1e6e9bff2f208e786ac345))
* implement wodt digital twin interface controller ([052c527](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/052c527fc987f2b72b0d89269a72a0850664def8))
* implement wodt web server ([8f5199f](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/8f5199ffa2d142e5e11c54b04d4dbbfae1ed3fa6))
* include dtd manager in the wodt digital adapter ([acc500a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/acc500a571061d0b27d7599181ec55ae55b6aa26))
* include platform management interface component inside the digital adapter ([c52e6a9](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/c52e6a919ac93268f01648a2874d3d2ad4d3f39b))
* include wodt web server in the wodt digital adapter ([053ac2a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/053ac2a101176eaab70a6e15908018b1126b76fb))
* make the dtkg observable by registered observers ([86f4b5b](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/86f4b5b66444a1de55be1050675340391e80bc97))


### Dependency updates

* **deps:** add javalin dependency ([46a569f](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/46a569f344b6e424bdf57c08a241ac6a4bd9624c))
* **deps:** add jena dependency ([15002d1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/15002d1475457c6a406189948a89e9a768f9b620))
* **deps:** add wldt dependency ([b04085a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/b04085a61b8d3c4041d7b940c1af899f5fdc9e1b))
* **deps:** add wot-servient dependency ([d570b86](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/d570b863d88467e70e35609533162da261e94e2a))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.82 ([bfb7e96](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/bfb7e96d092f5f338decb2b27819b4c4bf22ffbd))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.83 ([4c412e5](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/4c412e5f53f14af51db8069f077311177c6fc493))
* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.84 ([d0ee006](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/d0ee006a5dd774cdc6d3dca054149d255079112b))
* **deps:** update plugin com.gradle.enterprise to v3.16.1 ([249e85a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/249e85a967747cfe49745c787738ab15c27b3316))
* **deps:** update plugin java-qa to v1.29.0 ([416ccdc](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/416ccdc01ec7ce7f80d1c8437e50d22a35cb8978))


### Bug Fixes

* now pass instance key to digital twin state relationship deletion ([87ff0f1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/87ff0f19c2e4004aa466d2a128f94a300c0ce0c0))


### Tests

* adapt the configuration of the digital adapter in the integration test ([5fd1f0f](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/5fd1f0f266048cf3aadb50588a1518cba22002bd))
* add dtkg engine tests ([078c341](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/078c341a4276ac686f8c7cf2090649a9de577273))
* add main for the integration test ([52c3668](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/52c36688a4be9b20cf30db2072588e690c016bd8))
* add physical adapter for integration test ([9e6ee07](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/9e6ee073b19a028862685a32cc29fb343b4fbdd5))
* add relationship deletion to lamp dt ([5270d7e](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/5270d7e49a77f3f292da26156ceefef7e23ad8bb))
* add shadowing function for integration test ([43aa9c1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/43aa9c1b230196019432afb5f8d88023317548cd))
* add test resource to validate dtkg engine ([ae1b7a6](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/ae1b7a6a870b21d01fcb4517b43f5079977f1b25))
* add testing utils for reading resource files ([9db14b9](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/9db14b9061519ab4447225244ac513828cae0612))
* add tests for dtd manager ([63cb392](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/63cb3929e6f72e2c2fb844afc39c9fbe187f9684))
* add tests for wodt digital twin interface controller ([281fd97](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/281fd979e7765de639c866c20f7e3c1c2f6ea8d2))
* create ontology for lamp dt integration test ([0caa931](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/0caa93136d9cbf11d646afec8b0cef41d2a0cca8))
* re-add the relationship in integration test dt ([b9597fe](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/b9597fea7515f336214e099fb0a24be1fb7914c0))
* update physical adapter property ([fd4016d](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/fd4016de618bfdbb419d162a6e25215330a80d36))


### Build and continuous integration

* add git semantic version for project version ([6812b8b](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/6812b8bb3aeae8bda2192e6fd892e3ed6aed09a6))
* configure automatic delivery on github packages ([7cf1ad5](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/7cf1ad521c525e41da5a1c7ce6a26c29bb867c6d))
* configure delivery on github packages ([b3048ac](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/b3048ac32c4fb61b6a9589f47f23d8157a4508cd))
* set github actor environment variable ([b859bdd](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/b859bddb1c072959d3ecd45b1364a862add1c8f6))
* use java-library plugin ([d2d7765](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/d2d776545c066e63264a94b8f1dc9585b32f2d4b))


### General maintenance

* add dtkg engine interface ([38d1126](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/38d1126dd689737ca39acac0b155816ed480e78c))
* add elements necessary to dtd manager to digital adapter configuration ([d4260a8](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/d4260a811bc2c771988fed5725be9023f2488593))
* add platform to register to digital adapter configuration ([7cf72a1](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/7cf72a15d180a310c591f7cab49053f8dee30521))
* add the possibility to obtain the available actions from dtd ([6c0d127](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/6c0d1273d96ae5a76c23e0a0226c6f76ab704042))
* add the possibility to obtain the list of predicates from a blank node ([03bdb6c](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/03bdb6c64497a1d63068eec6f3a91def22a283a0))
* add web server controller interface ([c43c425](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/c43c425a26cffa316c1b746902bb8173e5af3e09))
* add wodt digital twin interface controller interface ([ce65b10](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/ce65b10ea3ec7242042486d27a84462a970d4da4))
* add wodt vocabulary ([7f6ad8d](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/7f6ad8d4217f723f8fc162983bd60c7c607ad7c2))
* create base wodt digital twin interface ([37d52b3](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/37d52b373f8afc843640d7d5b5a8e36f26e1c502))
* create blank node class ([2025cac](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/2025cac7c37440ad7c4c8f7709c2446321b7a9d9))
* create dt ontology interface ([adbad4a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/adbad4ac215182cbf146a3c30589c5f576a28e1c))
* create DTD Manager interface ([7c1b3ab](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/7c1b3abfe9ff6969eb615833cc66ddf44a7361c5))
* create individual class ([78ece17](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/78ece17d37c99a567fdf01a079b6d75246239702))
* create literal class ([1679918](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/16799186ad9bc5f9d3d479571e50b35a06984b2c))
* create Node interface ([56f3a7a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/56f3a7a4d42dc7dbc1f8dada5e3171c17df13390))
* create platform management interface ([817588c](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/817588cb5926b25f4179ce91a3098c6c140da438))
* create property class ([771aa1a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/771aa1a3a3101e76d9572f84e5a345ef5c582428))
* create Resource interface ([a7e1d2a](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/a7e1d2a9c73d2585e8dc894c24d234ba4feeebb6))
* create wodt web server interface ([ca292fc](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/ca292fc4d5c25a8dca70cca40894a47049f389ec))
* delete main file ([0003ae9](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/0003ae9eac166061767c863b86e8d024bc233a30))
* set the project name and group ([2297322](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/2297322bcd427d73c5596a47a97c6ce4f3f472ed))
* update README ([bd53c5c](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/bd53c5ce2f11dcb16424426afca550f9f4a6f68a))


### Refactoring

* apply isp to dtd manager interface ([ff9e370](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/ff9e3708e79e6806fb1c63cc431ec1e4659b6e19))
* apply isp to dtkg engine interface ([1402750](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/1402750c4932c0626cda18f8ee5f94dd2ee75478))
* change name of the digital adapter to allow better modularization ([5b45067](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/5b45067203027eb6d76ee4aa074399627b16af51))
* change package name ([3fa430f](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/3fa430fa2999ba08d348076c77eae9da84ccb061))
* create configuration for wodt digital adapter ([175a9fb](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/175a9fb3bbc20b2948b1bb632f338681ba0243b5))
* improve types ([ba8d296](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/ba8d296dc8b7efc93e7eaf34c729791d6802010c))
* move ontology model elements to ontology package ([b3a3fd8](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/b3a3fd8ec57ef2190c7ac5f8fe5d77c01b14ccfa))
* reduce visibility of components ([e9d88f2](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/e9d88f2d1e2eacecc0f3d43cb29da553c713c7f2))
* use configuration for wodt digital adapter ([d90d6f9](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/d90d6f99c6f22531c1f897fef32e1624c90d9f3b))
* use lambda expression for action's form addition ([4220fde](https://github.com/WebBased-WoDT/wldt-wodt-adapter/commit/4220fdea18cc5460338c5960a37407ded9e64f31))

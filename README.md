# FDPClient
[![State-of-the-art Shitcode](https://img.shields.io/static/v1?label=State-of-the-art&message=Shitcode&color=7B5804)](https://github.com/trekhleb/state-of-the-art-shitcode)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/laoshuikaixue/FDPClient)
![GitHub lines of code](https://tokei.rs/b1/github/laoshuikaixue/FDPClient)
![GitHub Star](https://img.shields.io/github/stars/laoshuikaixue/FDPClient?style=flat-square)
[![Build Status](https://img.shields.io/endpoint.svg?url=https%3A%2F%2Factions-badge.atrox.dev%2FSteamTools-Team%2FSteamTools%2Fbadge%3Fref%3Ddevelop&style=flat-square)](https://github.com/laoshuikaixue/FDPClient/actions/workflows/build.yml?query=event%3Apush)
[![Release Download](https://img.shields.io/github/downloads/laoshuikaixue/FDPClient/total?style=flat-square)](https://github.com/laoshuikaixue/FDPClient/releases)
[![Maintainability](https://api.codeclimate.com/v1/badges/a41ae7bde63c143e426a/maintainability)](https://codeclimate.com/github/UnlegitMC/FDPClient/maintainability)
![Minecraft](https://img.shields.io/badge/game-Minecraft-brightgreen)

[![Bilibili](https://img.shields.io/badge/bilibili-老水开学-blue.svg?style=flat-square&logo=bilibili)](https://space.bilibili.com/516771561)
[![YouTube](https://img.shields.io/badge/YouTube-LaoShui-blue.svg?style=flat-square&logo=youtube)](https://www.youtube.com/channel/UC0mJv6_9CMao8sT1we0zeDA)
[![QQ群](https://img.shields.io/badge/QQ群-1028574302-blue.svg?style=flat-square&color=12b7f5)](https://jq.qq.com/?_wv=1027&k=6gx9GOp5)

A free mixin-based injection hacked-client for Minecraft using Minecraft Forge based on LiquidBounce.

## Issues
If you notice any bugs or missing features, you can let us know by opening an issue [here](https://github.com/laoshuikaixue/FDPClientissues).

## License
This project is subject to the [GNU General Public License v3.0](LICENSE). This does only apply for source code located directly in this clean repository. During the development and compilation process, additional source code may be used to which we have obtained no rights. Such code is not covered by the GPL license.

For those who are unfamiliar with the license, here is a summary of its main points. This is by no means legal advise nor legally binding.

You are allowed to
- use
- share
- modify

this project entirely or partially for free and even commercially. However, please consider the following:

- **You must disclose the source code of your modified work and the source code you took from this project. This means you are not allowed to use code from this project (even partially) in a closed-source (or even obfuscated) application.**
- **Your modified application must also be licensed under the GPL** 

Do the above and share your source code with everyone; just like we do.

## Setting up a Workspace
FDPClient uses gradle, so make sure that it is installed properly. Instructions can be found on [Gradle's website](https://gradle.org/install/).
1. Clone the repository using `git clone https://github.com/laoshuikaixue/FDPClient.git`. 
2. CD into the local repository folder.
3. Depending on which IDE you are using execute either of the following commands:
    - For IntelliJ: `gradlew --debug setupDevWorkspace idea genIntellijRuns build`
    - For Eclipse: `gradlew --debug setupDevWorkspace eclipse build`
4. Open the folder as a Gradle project in your IDE.
5. Select the Forge run configuration.

## Additional libraries
### Mixins
Mixins can be used to modify classes at runtime before they are loaded. FDPClient uses it to inject its code into the Minecraft client. This way, we do not have to ship Mojangs copyrighted code. If you want to learn more about it, check out its [Documentation](https://docs.spongepowered.org/5.1.0/en/plugin/internals/mixins.html).

## Contributing
We are welcome for your contributing, but you have to follow these rules to make us merge your pull request.

### Language and Code Quality
First of all, your code at least can be build, and make bugs as less as you can!   
And we want to use kotlin features to make coding easier and faster, please use kotlin and make the [Detekt](https://github.com/detekt/detekt) code quality check good and use kotlin features if you can **\***, we never merge shitcode.

#### Kotlin features
Usage of kotlin features and help enhance the code readability    
kotlin feature examples
~~~kotlin
Timer().schedule(2000L) { 
    // your code
}
~~~
not using kotlin feature
~~~kotlin
Timer().schedule(object : TimerTask(){
    override fun run() {
        // your code
    }
},2000L)
~~~

### Skid codes
Please use original code if you can, and no direct code steals, but we welcome skid using packet logger or anything else like that to skid from a close source and make the cheating community more open!

### Useless features
Useless features means feature only you think its usable ,or feature can be have with config change.  
Like the "TimerSpeed" option to InfiniteAura, this feature can be added with bind Timer to the key with InfiniteAura, or use macro system in FDP Client.


## Contributing

We appreciate contributions. So if you want to support us, feel free to make changes to LiquidBounce+'s source code and submit a pull request. Currently, our main goals are the following:
1. Enhance the vision of the project.
2. Re-work most of the render code.

If you have experience in one or more of these fields, we would highly appreciate your support.

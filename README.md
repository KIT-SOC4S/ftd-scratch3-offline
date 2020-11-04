# ftd-scratch3-offline
[![Build Status](https://travis-ci.org/KIT-SOC4S/ftd-scratch3-offline.svg?branch=master)](https://travis-ci.org/KIT-SOC4S/ftd-scratch3-offline)

## Building
About 2-3 GB disk space are needed as the build has to download packages for many different architectures.
```shell
git clone https://github.com/KIT-SOC4S/ftd-scratch3-offline.git
cd ftd-scratch3-offline/
./gradlew check distribute
```
The distribution can be found inside the `ftd-ui-server/build/dist/` folder in an archive.

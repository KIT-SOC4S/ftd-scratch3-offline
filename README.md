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

## Building the scratch distribution
```shell
mkdir scratch
cd scratch
git clone https://github.com/LLK/scratch-blocks.git
cd scratch-blocks
git checkout 63679b2861cd318bc1cb2ef7b02b8a3d83355158
cd ..
git clone https://github.com/intrigus/scratch-vm.git
git clone https://github.com/intrigus/scratch-gui.git
```
Then run this command:
```shell
cd scratch-vm
npm install
npm link
cd ../scratch-blocks
npm install
npm link
cd ../scratch-gui
npm install
npm link scratch-vm scratch-blocks
npm run build
cd build
tar --exclude='*.map' -cf scratch_dist.tar .
sha256sum  scratch_dist.tar > scratch_dist.tar.sha256.txt 
```
You should then upload the resulting `scratch_dist.tar` and `scratch_dist.tar.sha256.txt` files somewhere, e.g. as a Github release.

**To update the scratch release that is distributed by default, go to https://github.com/KIT-SOC4S/ftd-scratch3-offline/blob/master/build.gradle
and replace the URL in `fetchMatchingScratchRelease` and replace the checksum in `verifyMatchingScratchRelease` with the one from `scratch_dist.tar.sha256.txt`.**

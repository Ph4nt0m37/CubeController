# CubeController

**CubeController** is a program that allows you to control your computer and play games using a Smartcube.

## Feature Overview
* Connect to a **wide variety** of Smartcubes
* Set **keybinds and timings** that correspond with moves on a Smartcube
* Save **presets** with keybinds and timings

## Installation
To install CubeContoller, go to the [Releases](https://github.com/Ph4nt0m37/CubeController/releases) tab and follow the installation instructions.

## How to Use
### To set a keybind:
1. Click the **?** symbol next to a move and then press the key you want to correspond with that move
2. Under that move, set how long you want that key to be pressed

### To unset a keybind:
1. Hold **Shift** whilst pressing a currently set keybind to unset it

### To connect a cube:
1. Press the **Connect** button. A bluetooth connection popup should appear
    * If a popup does not appear, make sure Bluetooth is enabled in your browser
2. Connect your cube. You should get a green **Connected** text under the button
 
<img width="787" height="527" alt="image" src="https://github.com/user-attachments/assets/4a24caa6-5914-4f07-8178-47362a3d06ef" />

## Supported Cubes
CubeController uses the [smartcube-web-bluetooth](https://github.com/poliva/smartcube-web-bluetooth) API for Smartcube connection, so any puzzles it supports should be supported here as well.

Here is a brief list that smartcube-web-bluetooth supports:
* GAN Smart Cubes (Gen1 / Gen2 / Gen3 / Gen4)
  * Until Gen5 is released, all GAN Smartcubes should be supported
* Giiker / Mi Smart / Hi- cubes
* GoCube / Rubik’s Connected
* MoYu smart cubes:
  * MoYu AI 2023 (GAN Gen2 protocol)
  * MoYu MHC smart cubes
  * MoYu WRM smart cubes (WCU_MY3)
* QiYi Smart Cubes and XMD Tornado V4


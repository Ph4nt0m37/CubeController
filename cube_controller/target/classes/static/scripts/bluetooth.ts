import {
    connectSmartCube,
} from 'smartcube-web-bluetooth';

import type {
    SmartCubeConnection,
    SmartCubeEvent
} from 'smartcube-web-bluetooth';

//page stuff
const connectButton = document.getElementById("connect-button");
const lastMoveText = document.getElementById("last-move");

// Connect to any supported smart cube
connectButton?.addEventListener("click", async ()=>{
    console.log("clicked");
    const conn: SmartCubeConnection = await connectSmartCube();
    console.log("connected");

    conn.events$.subscribe((event: SmartCubeEvent) => {
        if (event.type === "FACELETS") {
            console.log("Cube facelets state", event.facelets);
        } else if (event.type === "MOVE") {
            console.log("Cube move", event.move, "face", event.face, "direction", event.direction);
            if (lastMoveText) {
                console.log("MOVE_DONE");
                lastMoveText.textContent = event.move;
            }
        } else if (event.type === "GYRO") {
            console.log("Cube orientation quaternion", event.quaternion);
        } else if (event.type === "BATTERY") {
            console.log("Battery level", event.batteryLevel);
        }
    });

    // Request current facelets / battery if supported
    if (conn.capabilities.facelets) {
        await conn.sendCommand({ type: "REQUEST_FACELETS" });
    }
    if (conn.capabilities.battery) {
        await conn.sendCommand({ type: "REQUEST_BATTERY" });
    }
});
import {
    connectSmartCube,
} from 'smartcube-web-bluetooth';

import type {
    SmartCubeConnection,
    SmartCubeEvent
} from 'smartcube-web-bluetooth';

//page stuff
const connectButton = document.getElementById("connect-button");
const connectionText = document.getElementById("connection-text");
const lastMoveText = document.getElementById("last-move");

// Connect to any supported smart cube
connectButton?.addEventListener("click", async ()=>{
    if (connectionText) {
        connectionText.style.color = "#3498db";
        connectionText.textContent = "Attempting connection...";
    }
    
    const conn: SmartCubeConnection = await connectSmartCube();
    
    if (connectionText) {
        if (conn) {
            connectionText.textContent = "Connected";
            connectionText.style.color = "#22eb51";
        }else {
            connectionText.textContent = "Connection failed";
            connectionText.style.color = "#e23333";
        }
    }
    

    conn.events$.subscribe((event: SmartCubeEvent) => {
        if (event.type === "FACELETS") { //facelets is the current state of the cube. ex: UUUUUUUUURRRRRRRRRFFFFFFFFFDDDDDDDDDLLLLLLLLLBBBBBBBBB (solved)
            //console.log("Cube facelets state", event.facelets);
        } else if (event.type === "MOVE") {
            //console.log("Cube move", event.move, "face", event.face, "direction", event.direction);
            if (lastMoveText) {
                console.log(`MOVE_DONE|${event.move}`);
                lastMoveText.textContent = event.move;
            }
        } else if (event.type === "GYRO") {
            //console.log("Cube orientation quaternion", event.quaternion);
        } else if (event.type === "BATTERY") {
            //console.log("Battery level", event.batteryLevel);
        }
    });

    // // Request current facelets / battery if supported
    // if (conn.capabilities.facelets) {
    //     await conn.sendCommand({ type: "REQUEST_FACELETS" });
    // }
    // if (conn.capabilities.battery) {
    //     await conn.sendCommand({ type: "REQUEST_BATTERY" });
    // }
});
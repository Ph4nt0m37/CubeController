const keyBoxes = document.getElementsByClassName("key");
const timeInputs = document.getElementsByClassName("time-input");

let clickedBox: Element | null = null;

let shiftDown = false;

for (const keyBox of keyBoxes) {
    keyBox.addEventListener("click", ()=>{
        setTimeout(()=>{
            keyBox.removeAttribute("key");
            keyBox.removeAttribute("mouse");
            if (!shiftDown) {
                clickedBox = keyBox;
                keyBox.classList.add("selected-box");
            }else {  //set keybind text to ? if shift is pressed (aka: clear keybind)
                keyBox?.removeAttribute("time");
                if (keyBox) {
                    keyBox.textContent = "?";
                    const timeInput = keyBox.parentElement?.parentElement?.querySelector(".time-input") as HTMLInputElement;
                    if (timeInput)
                        timeInput.value = "";
                }
            }
            
        }, 10); //wait 10 ms to let the document click event process
    });
}

for (const timeInput of timeInputs) {
    timeInput.addEventListener("input", ()=>{
        timeInput.parentElement?.querySelector(".key")?.setAttribute("time",`${(timeInput as HTMLInputElement).value}`);
    });
}

document.addEventListener("keydown", (event)=> {
    if (clickedBox) {
        if (event.key==" ") {
            clickedBox.textContent = "Space";
            clickedBox.setAttribute("key", "space");
        }else {
            clickedBox.textContent = event.key;
            clickedBox.setAttribute("key", event.key);
        }
        
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
    }

    if (event.key=="Shift")
        shiftDown = true;
});

document.addEventListener("keyup", (event)=>{
    if (event.key=="Shift")
        shiftDown = false;
});

let preventContextMenu = false;

document.addEventListener("mousedown", (event)=> {
    if (clickedBox) {
        if (event.button===0) {
            clickedBox.textContent = "Left Click";
            clickedBox.setAttribute("mouse","LEFT_MOUSE_DOWN");
        }else if (event.button===1) {
            clickedBox.textContent = "Middle Click";
            clickedBox.setAttribute("mouse","MIDDLE_MOUSE_DOWN");
        }else if (event.button===2) {
            clickedBox.textContent = "Right Click";
            clickedBox.setAttribute("mouse","RIGHT_MOUSE_DOWN");
        }
        
        clickedBox.classList.remove("selected-box");
        clickedBox = null;
        preventContextMenu = true;
    }
});

document.addEventListener("mouseup", (event)=> {
    if (event.button===2) {
        setTimeout(()=>{
            preventContextMenu = false;
        }, 10); //wait 10 ms for contextmenu event listener to process
    }
});

document.addEventListener("contextmenu", (event)=> {
    if (preventContextMenu) {
        event.preventDefault();
    }
});





class Preset {
    private id: number;
    private name: string;

    constructor(id: number, name: string) {
        this.id = id;
        this.name = name;
    }

    getId() {
        return this.id;
    }

    getName() {
        return this.name;
    }
}

const connectionText = document.getElementById("connection-text");

let presetDropdown: HTMLSelectElement = document.getElementById("preset-dropdown") as HTMLSelectElement;
let saveProfileButton = document.getElementById("save-profile-button");
let deleteProfileButton = document.getElementById("delete-profile-button");

let currentPreset: Preset | null = null;

saveProfileButton?.addEventListener("click",()=>{
    const selectedPreset = presetDropdown.options[presetDropdown.selectedIndex];
    let promptName: string = prompt("What is the name of this preset?", selectedPreset?.textContent) as string;
    const presetId = currentPreset!=null ? currentPreset.getId() : Math.floor(Math.random()*9999999);
    fetch("/save-preset",{
        method: "POST",
        body: JSON.stringify({
            id: presetId,
            name: promptName
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    }).then(resp=>{
        if (resp.ok) return;
        console.error("Something went wrong saving this preset.");
        if (connectionText) {
            connectionText.textContent = "Failed To Save Preset";
            connectionText.style.color = "#e23333";
        }
    }).then(()=>{
        if (currentPreset==null) {
            addPresetToList(new Preset(presetId, promptName), true);
        }else {
            //renaming capability
            const presetOption = document.querySelector(`[presetid="${presetId}"]`);
            if (presetOption)
                presetOption.textContent = promptName;
        }
        currentPreset = new Preset(presetId, promptName);
        selectedPreset?.setAttribute("presetId",String(presetId));
        if (connectionText) {
            connectionText.textContent = "Preset Saved Successfully";
            connectionText.style.color = "#22eb51";
        }
        
    });
});

deleteProfileButton?.addEventListener("click",()=>{
    if (currentPreset!=null && confirm("Are you sure you want to delete this preset?")) {
        fetch("/save-preset",{
            method: "DELETE",
            body: JSON.stringify({
                id: currentPreset.getId(),
                name: currentPreset.getName()
            }),
            headers: {
                "Content-type": "application/json; charset=UTF-8"
            }
        }).then(resp=>{
            if (resp.ok) return;
            console.error("Something went wrong deleting this preset.");
            if (connectionText) {
                connectionText.textContent = "Failed To Delete Preset";
                connectionText.style.color = "#e23333";
            }
        }).then(()=>{
            deletePresetFromList(new Preset(Number(currentPreset?.getId()), String(currentPreset?.getName())));
        });
    }
});

presetDropdown?.addEventListener("change",()=>{
    if (presetDropdown.value == "default") {
        currentPreset = null;
        loadKeyMapping({});
        return;
    }

    const selectedPreset = presetDropdown.options[presetDropdown.selectedIndex];
    currentPreset = new Preset(Number(selectedPreset?.getAttribute("presetId")),String(selectedPreset?.textContent));
    fetch("/presets.json").then((promise)=>{
        if (promise.ok) return promise.json();
        console.error("Something went wrong loading this preset.");
    }).then((data)=>{
        loadKeyMapping(data[String(currentPreset?.getId())]["moveMap"]);
    });
});

function addPresetToList(currentPreset: Preset, select: boolean) {
    const presetTemplate = document.getElementById("preset-option-template");
    const newPresetNode: HTMLOptionElement | undefined = presetTemplate?.cloneNode(true) as HTMLOptionElement;
    newPresetNode.textContent = currentPreset.getName();
    newPresetNode?.setAttribute("presetid",String(currentPreset.getId()));
    newPresetNode.value = currentPreset.getName();
    presetDropdown.appendChild(newPresetNode);
    if (select) {
        const currSelectedOption = presetDropdown.options[presetDropdown.selectedIndex];
        if (currSelectedOption) currSelectedOption.selected = false;
        newPresetNode.selected = true;
    }
}

function deletePresetFromList(currentPreset: Preset) {
    const presetTemplate = document.getElementById("preset-option-template") as HTMLOptionElement;
    presetDropdown?.options[presetDropdown.selectedIndex]?.remove();
    presetTemplate.selected = true;
    loadKeyMapping({});
}

function loadKeyMapping(keyMapping: object) {
    const moveList: string[] = ["U", "U'","D", "D'","F", "F'","B", "B'","R", "R'","L", "L'"];

    for (const move of moveList) {
        const keyBox = document.querySelector(`.${CSS.escape(move)}`);
        keyBox?.removeAttribute("key");
        keyBox?.removeAttribute("mouse");
        const moveData = keyMapping[move as keyof object];
        if (moveData) {
            if (moveData["key"]) keyBox?.setAttribute("key", moveData["actionString"]);
            if (moveData["mouseEvent"]) keyBox?.setAttribute("mouse", moveData["actionString"]);
            keyBox?.setAttribute("time", moveData["timeMs"]);
            if (keyBox) {
                keyBox.textContent = beautifyActionString(moveData["actionString"]);
                const timeInput = keyBox.parentElement?.parentElement?.querySelector(".time-input") as HTMLInputElement;
                if (timeInput)
                    timeInput.value = moveData["timeMs"];
            }
        }else {
            keyBox?.removeAttribute("key");
            keyBox?.removeAttribute("mouse");
            if (keyBox) {
                keyBox.textContent = "?";
                const timeInput = keyBox.parentElement?.parentElement?.querySelector(".time-input") as HTMLInputElement;
                if (timeInput)
                    timeInput.value = "";
            }
        }
    }
}

function beautifyActionString(actionString: string): string {
    switch (actionString) {
        case "space": return "Space";
        case "LEFT_MOUSE_DOWN": return "Left Click";
        case "MIDDLE_MOUSE_DOWN": return "Middle Click";
        case "RIGHT_MOUSE_DOWN": return "Right Click";
        default: return actionString;
    }  
}

function loadAllPresets() {
    fetch("/presets.json").then((promise)=>{
        if (promise.ok) return promise.json();
        console.error("Something went wrong loading presets.");
    }).then((data)=>{
        const presetTemplate = document.getElementById("preset-option-template") as HTMLOptionElement;
        for (const presetIdStr of Object.keys(data)) {
            addPresetToList(new Preset(Number(presetIdStr), data[presetIdStr]["name"]), false);
        }
        //keep template selected
        presetTemplate.selected = true;
    });
}

loadAllPresets();


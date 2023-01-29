import React from "react";

const MenuItem = ({item, onMenu}) => {

    const buttonstyle = {
        width: "300px",
        backgroundcolor: "blue",
        border: "none",
        color: "black",
        padding: "15px",
        cursor: "pointer"
    }

    const {name} = item
    return (
        <div className="menu" onClick={() =>onMenu(name)}>
            <button style={buttonstyle}>{name}</button>
        </div>
    );
};

export default MenuItem;
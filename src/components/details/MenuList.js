import React from "react";
import MenuItem from "./MenuItem";

const MenuList = ({menu, onMenu}) => {

    const menuliststyle = {
        margin: "100px",
        display: "flex",
        flexDirection: "row",
        align: "center"
    }

    return(
        <div className="menulist" style={menuliststyle}>
            {
                menu.map((item, index)=><MenuItem key={index} item={item} onMenu={onMenu}/>)
            }
        </div>
    );
};

export default MenuList;
import React from "react";
import GiftItem from "./GiftItem";

const GiftList = ({data}) => {

    const itemliststyle = {
        margin: "80px",
        display: "flex",
        flexDirection: "row"
    }

    return(
        <div className="list" style={itemliststyle}>
            {
                data.map(item => <GiftItem key={item.id} item={item} />)
            }
        </div>
    );
};

export default GiftList;
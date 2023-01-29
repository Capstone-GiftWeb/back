import { getSuggestedQuery } from "@testing-library/react";
import React from "react";

const GiftItem = ({item}) => {

    const itemstyle = {
        border: "1px solid grey",
        margin: "20px",
        borderRadius: "5px"
    }

    const imgstyle = {
        width: "250px",
        height: "350px"
    }

    const{id, img, name, title, age, price, cate, anniversary, relation}= item
    
    return(
        <div className="item" style={itemstyle}>
            <img src={img} alt={title} style={imgstyle}/>
            <h3>제품명 : {name}</h3>
            <h4>{title}</h4>
            <p>가격 : {price}원</p>
        </div>
    );
};

export default GiftItem;
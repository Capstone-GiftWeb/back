import React, { useEffect, useState } from 'react';
import Nav from '../main/Nav'
import menulist from './Menu';
import MenuList from './MenuList';
import GiftList from './GiftList';

const Price = () => {
    const [data, setData] = useState(menulist);
    const [menu, setMenu] = useState([
        {name : '전체'},
        {name : '1만원대',},
        {name : '2만원대'},
        {name : '3만원대'},
        {name : '4만원이상'},
    ]);

    const onMenu = (name) => {
        if(name === '전체'){
            setData(menulist)
        }else{
            setData(menulist.filter(item => item.price === name))
        }
    }

    return (
        <>
            <div className='Age'>
                <Nav />
                <br />
                <br />
                <br />
                <MenuList menu={menu} onMenu={onMenu} />
                <GiftList data={data}/>
            </div>
        </>
    )
}

export default Price;
import React, { useEffect, useState } from 'react';
import Nav from '../main/Nav'
import menulist from './Menu';
import MenuList from './MenuList';
import GiftList from './GiftList';

const Anniversary = () => {
    const [data, setData] = useState(menulist);
    const [menu, setMenu] = useState([
        {name : '전체'},
        {name : '생일'},
        {name : '집들이'},
        {name : '크리스마스'},
        {name : '명절'},
    ]);

    const onMenu = (name) => {
        if(name === '전체'){
            setData(menulist)
        }else{
            setData(menulist.filter(item => item.anniversary === name))
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

export default Anniversary;
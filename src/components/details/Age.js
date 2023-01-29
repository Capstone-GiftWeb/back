import React, { useEffect, useState } from 'react';
import Nav from '../main/Nav'
import menulist from './Menu';
import MenuList from './MenuList';
import GiftList from './GiftList';

const Age = () => {
    const [data, setData] = useState(menulist);
    const [menu, setMenu] = useState([
        {name : '전체'},
        {name : '10대'},
        {name : '20대'},
        {name : '30대'},
    ]);

    const onMenu = (name) => {
        if(name === '전체'){
            setData(menulist)
        }else{
            setData(menulist.filter(item => item.age === name))
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

export default Age;
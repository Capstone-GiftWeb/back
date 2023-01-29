import React, { useEffect, useState } from 'react';
import Nav from '../main/Nav'
import menulist from './Menu';
import MenuList from './MenuList';
import GiftList from './GiftList';

const Relationship = () => {
    const [data, setData] = useState(menulist);
    const [menu, setMenu] = useState([
        {name : '전체'},
        {name : '연인'},
        {name : '가족'},
        {name : '친구'},
        {name : '직장'}
    ]);

    const onMenu = (name) => {
        if(name === '전체'){
            setData(menulist)
        }else{
            setData(menulist.filter(item => item.relation === name))
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

export default Relationship;
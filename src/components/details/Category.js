import React, { useEffect, useState } from 'react';
import Nav from '../main/Nav'
import menulist from './Menu';
import MenuList from './MenuList';
import GiftList from './GiftList';

const Category = () => {
    const [data, setData] = useState(menulist);
    const [menu, setMenu] = useState([
        {name : '전체'},
        {name : '명품'},
        {name : '음식'},
        {name : '옷'},
        {name : '가구'},
    ]);

    const onMenu = (name) => {
        if(name === '전체'){
            setData(menulist)
        }else{
            setData(menulist.filter(item => item.cate === name))
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

export default Category;
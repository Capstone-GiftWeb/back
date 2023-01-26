import React, { useEffect, useState } from 'react';
import '../style/Nav.css'

const Nav = () => {
    return (
        <header>
            <div className="header--logo">
                <a href="/" style={{ textDecoration: "none", color: "black"}}>
                    <img src="img/mainNavIcon.png" alt="home_logo" />
                </a>
                <a href="/" style={{ textDecoration: "none", color: "black"}}>
                    상상부기
                </a>
            </div>
            <div className="header--link">
                <a href="/"style={{ textDecoration: "none", color: "black"}}>홈</a>
                <a href="/Price" style={{ textDecoration: "none", color: "black"}}>가격별</a>
                <a href="/Relationship" style={{ textDecoration: "none", color: "black"}}>관계별</a>
                <a href="/Age" style={{ textDecoration: "none", color: "black"}}>연령별</a>
                <a href="/Anniversary" style={{ textDecoration: "none", color: "black"}}>기념일</a>
                <a href="/Category" style={{ textDecoration: "none", color: "black"}}>카테고리</a>
            </div>
            <div className="header--user">
                <a href='/profile'>
                    <img src="http://occ-0-3996-1361.1.nflxso.net/dnm/api/v6/K6hjPJd6cR6FpVELC5Pd6ovHRSk/AAAABY5cwIbM7shRfcXmfQg98cqMqiZZ8sReZnj4y_keCAHeXmG_SoqLD8SXYistPtesdqIjcsGE-tHO8RR92n7NyxZpqcFS80YfbRFz.png?r=229" alt="User" />
                </a>
            </div>
        </header>
    );
};

export default Nav;
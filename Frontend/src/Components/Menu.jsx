import React from 'react'
import '../styles/Menu.css'
import {Link, NavLink } from 'react-router-dom'

function Menu() {
    return (
        <div className='Navbars'>
            <ul className='NavbarsWrappers'>
                <li className='NavbarElement'>
                    <NavLink className='link' to='/home'>Home</NavLink>
                </li>
                <li className='NavbarElement'>
                    <NavLink className='link' to='/category'>Category</NavLink>
                </li>
                <li className='NavbarElement'>
                    <NavLink className='link' to='/sign-in'>Sign In</NavLink>
                </li>
                <li className='Navbutton'>
                    <NavLink className='linkbtn' to='/sign-up'>Sign Up</NavLink>
                </li>
            </ul>
        </div>
    )
}

export default Menu
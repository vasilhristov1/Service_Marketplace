import React, { useEffect, useState } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';
import { GiHamburgerMenu } from 'react-icons/gi';
import { ImCross } from 'react-icons/im';
import { jwtDecode } from "jwt-decode";
import { CgProfile } from "react-icons/cg";

function Navbar({ clicked, isClicked }) {
    const navigate = useNavigate();
    const userRole = localStorage.getItem('Jwt_Token') ? jwtDecode(localStorage.getItem('Jwt_Token'))?.role : null;
    const [token, setToken] = useState('');

    useEffect(() => {
        setToken(localStorage.getItem('Jwt_Token'));
        if (token) {
            navigate('/home-page');
        }
    }, []);

    const handleClicked = () => {
        isClicked(!clicked);
        console.log('clicked');
    };

    const handleLogout = async () => {
        try {
            localStorage.removeItem('Jwt_Token');
            navigate('/home-page');
        } catch (error) {
            console.error('Error during logout:', error);
        }
    };

    const handlePagination = async () => {
        try {
            sessionStorage.removeItem('PageNumber');
        } catch (error) {
            console.error('Error during logout:', error);
        }
    }

    return (
        <div className="Nav">
            <ul className="NavbarWrapper">
                <li className="NavLogo">
                    <Link className="Link" to="/" onClick={handlePagination}>
                        Service Marketplace
                    </Link>
                </li>
                <li className="NavElements">
                    <NavLink className="Link" to="/home-page" onClick={handlePagination}>
                        Home
                    </NavLink>
                </li>
                <li className="NavElements">
                    <NavLink className="Link" to="/services" onClick={handlePagination}>
                        Services
                    </NavLink>
                </li>
                {userRole && userRole.includes('PROVIDER') && (
                    <li className="NavElements">
                        <NavLink className="Link" to="/add-service-page" onClick={handlePagination}>
                            Add Service
                        </NavLink>
                    </li>
                )}

                {localStorage.getItem('Jwt_Token') ? (
                    <>
                        <li className="Profile-Button">
                            <NavLink className="Link" to="/profile" onClick={handlePagination}>
                                <CgProfile className="UserIcon" />
                            </NavLink>
                        </li>
                        <li className="logout-button">
                            <NavLink className="BtnLink" onClick={handleLogout}>
                                Logout
                            </NavLink>
                        </li>
                    </>
                ) : (
                    <>
                        <li className="NavButton">
                            <NavLink className="BtnLink" to="/sign-in">
                                Sign In
                            </NavLink>
                        </li>
                        <li className="NavButton">
                            <NavLink className="BtnLink" to="/sign-up">
                                Sign Up
                            </NavLink>
                        </li>
                    </>
                )}
            </ul>
            {!clicked ? (
                <GiHamburgerMenu onClick={handleClicked} className="Icon" />
            ) : (
                <ImCross onClick={handleClicked} className="Icon" />
            )}
        </div>
    );
}

export default Navbar;

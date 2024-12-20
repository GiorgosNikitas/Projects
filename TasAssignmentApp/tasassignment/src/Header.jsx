import React, { Component } from "react";
import {Link} from "react-router-dom";

class Header extends Component {
    render() {
        return (
            <nav id={"navbar"}>
                <ul>
                    <li>
                        <Link to={"/"}>Home</Link>
                    </li>
                </ul>

                <ul>
                    <li>
                        <Link to={"/courses"}>Courses</Link>
                    </li>
                </ul>

                <ul>
                    <li>
                        <Link to={"/students"}>Students</Link>
                    </li>
                </ul>

                <ul>
                    <li>
                        <Link to={"/assignment"}>Assignment</Link>
                    </li>
                </ul>

                <ul>
                    <li>
                        <Link to={"/statistics"}>Statistics</Link>
                    </li>
                </ul>
            </nav>
        );
    }
}

export default Header;

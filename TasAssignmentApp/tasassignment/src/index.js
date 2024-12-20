import React from "react";
import ReactDOM from "react-dom/client";
import "./CSS/index.css";
import reportWebVitals from "./reportWebVitals";
import "bootstrap/dist/css/bootstrap.min.css";
// import 'bootstrap/dist/css/bootstrap.min.css';
import {BrowserRouter} from "react-router-dom";
import App from "./App";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <App></App>
        </BrowserRouter>
    </React.StrictMode>
);

reportWebVitals();

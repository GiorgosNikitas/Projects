import React, { Component } from "react";
import "./FileUploader"
import FileUploader from "./FileUploader";
import "./CSS/fileUploader.css";

class General extends Component {
    render() {
        return (
            <div id={"general-container"} className={"d-flex h-100 align-items-center"}>
                <div className={"row justify-content-center align-items-center"}>
                    <div className={"col-3 justify-content-center"}>
                        <FileUploader label={"Courses"} role={"assistants"}/>
                    </div>
                    <div className={"col-3 justify-content-center"}>
                        <FileUploader label={"Students' information"} role={"students"}/>
                    </div>
                    <div className={"col-3 justify-content-center"}>
                        <FileUploader label={"Student's grades"} role={"grades"}/>
                    </div>
                </div>
            </div>
        );
    }
}

export default General;


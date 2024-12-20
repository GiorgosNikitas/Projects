import axios from "axios";
import React, { Component, useState } from "react";
// import "./CSS/fileUploader.css";

class FileUploader extends Component {
    state = {
        label: null,
        selectedFile: null,
        role: null
    };

    constructor(props) {
        super(props);
        this.state = {
            label: props.label,
            role: props.role
        };
    }

    onFileChange = (event) => {
        const file = event.target.files[0];
        console.log(file);
        if (file) {
            event.preventDefault();
            const url = "http://localhost:8080/api/upload" + this.state.role;
            console.log(url);
            const formData = new FormData();
            formData.append('file', file);
            formData.append('fileName', file.name);
            const config = {
                headers: {
                    'content-type': 'multipart/form-data',
                },
            };
            axios.post(url, formData, config).then((response) => {
                console.log(response.data);
            });
        }
    };

    render() {
        return (
            <div className={"file-uploader-container d-flex flex-column align-items-center"}>
                <div className={"justify-content-start"}>
                    <label className={"text-center"}>{this.state.label}</label>
                </div>
                <div className="mb-3">
                    <label htmlFor="formFile" className="btn btn-primary">Upload Courses File</label>
                    <input
                        className="form-control"
                        type="file"
                        accept={"text/csv"}
                        id="formFile"
                        onChange={this.onFileChange}
                    />
                </div>
            </div>
        );
    }
}

export default FileUploader;

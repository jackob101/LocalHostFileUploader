import React from "react";
import { Link } from "react-router-dom";

const DropdownMenu = (props) => {
    const generateDownload = () => {
        if (!props.file.directory)
            return (
                <div className="dropdown-item w-100">
                    <button
                        className="btn btn-link text-light text-decoration-none w-100"
                        onClick={() => props.downloadFile(props.file.name)}
                    >
                        Download
                    </button>
                </div>
            );
    };

    const generateContentEdit = () => {
        if (!props.file.directory)
            return (
                <div className="dropdown-item">
                    <Link
                        className="btn btn-link text-light text-decoration-none w-100"
                        to={{
                            pathname: "/note",
                            state: {
                                name: props.file.name,
                                path: props.file.path,
                                editing: true,
                            },
                        }}
                    >
                        Edit Content
                    </Link>
                </div>
            );
    };

    const generateNameEdit = () => {
        return (
            <div className="dropdown-item">
                <button
                    className="btn btn-link text-light text-decoration-none w-100"
                    onClick={() => props.toggleEdit(props.index)}
                >
                    Edit name
                </button>
            </div>
        );
    };

    const generateDelete = () => {
        return (
            <div className="dropdown-item">
                <button
                    className="btn btn-link text-light text-decoration-none w-100"
                    onClick={() =>
                        props.deleteFile(
                            props.file.path,
                            props.file.name,
                            props.file.directory
                        )
                    }
                >
                    Delete
                </button>
            </div>
        );
    };

    return (
        <div className="dropdown">
            <button
                className="btn btn-primary dropdown-toggle"
                type="button"
                id="dropdownMenuDark"
                data-bs-toggle="dropdown"
                aria-expanded="false"
            >
                Menu
            </button>
            <ul
                className="dropdown-menu dropdown-menu-dark"
                aria-labelledby="dropdownMenuDark"
            >
                {generateDownload()}
                {generateContentEdit()}
                {generateNameEdit()}
                {generateDelete()}
            </ul>
        </div>
    );
};

export default DropdownMenu;

import React, { useState } from "react";
import { Link } from "react-router-dom";

const FileItem = ({
    file,
    index,
    onEnterDirectory,
    downloadImage,
    submitEdit,
    toggleEdit,
    isEditing,
    deleteFile,
}) => {
    let options = {
        day: "numeric",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    };
    let date = new Date(file.modifiedDate + "Z");

    const modified =
        "Last modified on: " + date.toLocaleDateString("en-US", options);

    const [newName, setNewName] = useState(file.name);

    const onChange = (e) => {
        setNewName(e.target.value);
    };

    console.log(file);

    return (
        <div className="d-flex flex-row my-2 align-items-center">
            <div className="me-3 fw-bold">{index + 1}</div>
            {isEditing ? (
                <div>
                    <input type="text" onChange={onChange} value={newName} />
                    <button
                        className="btn btn-outline-primary"
                        onClick={() => {
                            toggleEdit(index);
                            submitEdit(file.path, file.name, newName);
                        }}
                    >
                        Submit
                    </button>
                </div>
            ) : file.directory ? (
                <button
                    className="btn-link btn m-0 p-0"
                    onClick={() => onEnterDirectory(file.name)}
                    title={modified}
                >
                    {file.name}
                </button>
            ) : (
                <span title={modified}>{file.name}</span>
            )}

            <div className="flex-grow-1"></div>

            {!file.directory ? (
                <>
                    <button
                        className="btn btn-link mx-1"
                        onClick={() => downloadImage(file.name)}
                    >
                        <img
                            src="/download-solid.svg"
                            alt="Download"
                            width="16px"
                        />
                    </button>
                    <Link
                        to={{
                            pathname: "/note",
                            state: {
                                name: file.name,
                                path: file.path,
                                editing: true,
                            },
                        }}
                    >
                        Edit
                    </Link>
                </>
            ) : (
                ""
            )}

            <button className="btn btn-link" onClick={() => toggleEdit(index)}>
                <img src="/edit-solid.svg" alt="Edit" width="16px" />
            </button>

            <button
                className="btn btn-link"
                onClick={() => deleteFile(file.path, file.name, file.directory)}
            >
                <img src="/trash-solid.svg" alt="Delete" width="16px" />
            </button>
        </div>
    );
};

export default FileItem;

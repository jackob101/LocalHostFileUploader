import React, { useState } from "react";

const FileItem = ({
    file,
    index,
    onEnterDirectory,
    downloadImage,
    submitEdit,
    toggleEdit,
    isEditing,
}) => {
    let options = {
        day: "numeric",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    };
    let date = new Date(file.modifiedDate + "Z");

    const [newName, setNewName] = useState(file.name);

    const onChange = (e) => {
        setNewName(e.target.value);
    };

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
                >
                    {file.name}
                </button>
            ) : (
                <span>{file.name}</span>
            )}

            <button
                className="btn btn-outline-primary"
                onClick={() => toggleEdit(index)}
            >
                Edit
            </button>

            <div className="ms-auto">
                <span>{date.toLocaleDateString("en-US", options)}</span>
            </div>

            {!file.directory ? (
                <button
                    className="btn btn-outline-primary mx-1"
                    onClick={() => downloadImage(file.name)}
                >
                    Download
                </button>
            ) : (
                ""
            )}
        </div>
    );
};

export default FileItem;

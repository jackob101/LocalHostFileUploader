import React from "react";

const FileItem = ({ file, index, onEnterDirectory, downloadImage }) => {
    let options = {
        day: "numeric",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    };
    let date = new Date(file.modifiedDate + "Z");

    return (
        <div className="d-flex flex-row my-2 align-items-center">
            <div className="me-3 fw-bold">{index + 1}</div>
            {file.directory && (
                <button
                    className="btn-link btn m-0 p-0"
                    onClick={() => onEnterDirectory(file.name)}
                >
                    {file.name}
                </button>
            )}
            {!file.directory && <span>{file.name}</span>}

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

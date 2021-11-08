import React, { useState } from "react";
import FileItem from "../FileItem/FileItem";

const FilesList = (props) => {
    let index = 0;

    const [editing, setEditing] = useState(-1);

    const toggleEdit = (index) => {
        setEditing(editing === index ? -1 : index);
    };

    return (
        <div className="flex-grow-1 my-2 p-3">
            <div
                className="d-flex flex-grow-1 flex-column overflow-auto"
                style={{ maxHeight: "560px" }}
            >
                {props.files.map((entry) =>
                    entry.map((entry) => {
                        let item = (
                            <FileItem
                                file={entry}
                                key={index}
                                index={index}
                                downloadImage={props.downloadImage}
                                onEnterDirectory={props.onEnterDirectory}
                                submitEdit={props.submitEdit}
                                isEditing={editing === index}
                                toggleEdit={toggleEdit}
                                deleteFile={props.deleteFile}
                            />
                        );
                        index++;
                        return item;
                    })
                )}
            </div>
        </div>
    );
};

export default FilesList;

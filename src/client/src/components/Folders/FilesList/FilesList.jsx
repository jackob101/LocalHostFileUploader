import React from "react";
import FileItem from "../FileItem/FileItem";

const FilesList = (props) => {
  return (
    <div className="flex-grow-1 my-2 p-3">
      <div
        className="d-flex flex-grow-1 flex-column overflow-auto"
        style={{ maxHeight: "560px" }}
      >
        {props.files.map((entry, key) => (
          <FileItem
            file={entry}
            key={key}
            index={key}
            onEnterDirectory={props.onEnterDirectory}
          />
        ))}
      </div>
    </div>
  );
};

export default FilesList;

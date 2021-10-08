import React from "react";
import FileItem from "../FileItem/FileItem";
import FileListControls from "../FileListControls";

const FilesList = (props) => {
  return (
    <div className="mx-auto my-2 w-100">
      {props.files.map((entry, key) => (
        <FileItem
          file={entry}
          key={key}
          index={key}
          onEnterDirectory={props.onEnterDirectory}
        />
      ))}
    </div>
  );
};

export default FilesList;

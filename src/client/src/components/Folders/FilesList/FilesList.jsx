import React from "react";
import FileItem from "../FileItem/FileItem";

const FilesList = (props) => {
  return (
    <div className="mx-auto my-2 w-100 p-3">
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

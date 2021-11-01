import React from "react";
import FileItem from "../FileItem/FileItem";

const FilesList = (props) => {
  let index = 0;
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
                onEnterDirectory={props.onEnterDirectory}
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

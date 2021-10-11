import React from "react";
import ToUploadItem from "./ToUploadItem";

const ToUploadList = (props) => {
  return (
    <div className="d-flex flex-column">
      {props.files.map((entry, key) => (
        <ToUploadItem file={entry} index={key} key={key} />
      ))}
    </div>
  );
};

export default ToUploadList;

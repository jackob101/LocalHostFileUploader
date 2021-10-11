import React from "react";

const ToUploadItem = (props) => {
  return (
    <div className="d-flex flex-row my-2">
      <span className="fw-bold mx-3">{"#" + props.index}</span>
      <span>{props.file.name}</span>
    </div>
  );
};

export default ToUploadItem;

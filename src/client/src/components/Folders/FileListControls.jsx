import React from "react";

const FileListControls = ({ onGoToParentDir, text, path }) => {
  return (
    <div className="d-flex flex-row">
      <button
        className="btn btn-link m-0 p-0"
        onClick={onGoToParentDir}
        disabled={path.length === 0}
      >
        {text}
      </button>
    </div>
  );
};

export default FileListControls;

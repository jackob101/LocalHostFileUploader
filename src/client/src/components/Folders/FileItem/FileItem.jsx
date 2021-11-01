import React from "react";

const FileItem = ({ file, index, onEnterDirectory }) => {
  return (
    <div className="d-flex flex-row my-2">
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
    </div>
  );
};

export default FileItem;

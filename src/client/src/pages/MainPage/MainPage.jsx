import axios from "axios";
import React, { useEffect, useState } from "react";
import ControllsList from "../../components/Folders/Controlls/ControllsList";
import FilesList from "../../components/Folders/FilesList/FilesList";
import NewDirectoryForm from "../../components/Folders/NewDirectoryForm";
import PathBreadcrumb from "../../components/Folders/PathBreadcrumb/PathBreadcrumb";
import UploadPanel from "../../components/Folders/UploadPanel/UploadPanel";
import styles from "./MainPage.module.css";

const MainPage = () => {
  const [files, setFiles] = useState([]);
  const [path, setPath] = useState([]);

  useEffect(() => {
    axios
      .get("https://localhost:8443/api/getFiles", {
        params: {
          path: path.join("/"),
        },
      })
      .then((response) => {
        console.log(response);
        setFiles(response.data.files);
      });
  }, [path]);

  const changeCurrentPath = (pathIndex) => {
    setPath(path.splice(0, pathIndex));
  };

  const onEnterDirectory = (directory) => {
    const newPath = [...path, directory];
    setPath(newPath);
  };

  const onGoToParentDir = () => {
    let newPath = [...path];
    newPath.pop();
    setPath(newPath);
  };

  const onFilesSubmit = (event, uploadedFiles) => {
    event.preventDefault();
    let formData = new FormData();
    uploadedFiles.map((entry) => {
      formData.append("files", entry);
    });
    console.log(path.join("/"));
    formData.append("path", path.join("/"));

    axios
      .post("https://localhost:8443/api/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
      .then((response) => {
        console.log(response);
        console.log(files);
        let newState = files.concat(response.data.files);
        console.log(files);
        console.log(newState);
        setFiles(newState);
      });
  };

  const onCreateNewFolder = (event, name) => {
    event.preventDefault();
    let formData = new FormData();
    formData.append("path", path.join("/"));
    formData.append("directoryName", name);

    axios
      .post("https://localhost:8443/api/create_directory", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
      .then((response) => {
        console.log(response);
        setFiles([...files, response.data.directory]);
      });
  };

  return (
    <div className="container h-100">
      <div className="row h-100">
        <div className="col-md-4 col-8">
          <UploadPanel onSubmit={onFilesSubmit} />
        </div>
        <div className="col d-flex flex-column border-start border-5  border-end">
          <div className="flex-grow-1 d-flex flex-column">
            <div className="p-2 border-bottom border-3">
              <PathBreadcrumb
                path={path}
                changeCurrentPath={changeCurrentPath}
              />
            </div>
            <div className="d-flex flex-column flex-grow-1">
              <ControllsList
                changeCurrentPath={changeCurrentPath}
                onGoToParentDir={onGoToParentDir}
              />

              <FilesList
                path={path}
                files={files}
                changeCurrentPath={changeCurrentPath}
                onGoToParentDir={onGoToParentDir}
                onEnterDirectory={onEnterDirectory}
              />
            </div>
            <div className="border-3 border-top p-3" style={{ height: "10%" }}>
              <NewDirectoryForm onCreateNewFolder={onCreateNewFolder} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainPage;

import { useEffect, useState } from "react";
import axios from "axios";

const useListService = () => {
  const [files, setFiles] = useState([]);
  const [path, setPath] = useState([]);

  const sortFileList = (fileList) => {
    let directories = [];
    let files = [];

    fileList.forEach((element) =>
      element.directory ? directories.push(element) : files.push(element)
    );

    directories.sort((a, b) => a.name.localeCompare(b.name));
    files.sort((a, b) => a.name.localeCompare(b.name));

    setFiles([directories, files]);
  };

  useEffect(() => {
    axios
      .get("https://localhost:8443/api/getFiles", {
        params: {
          path: path.join("/"),
        },
      })
      .then((response) => {
        sortFileList(response.data.files);
      });
  }, [path]);

  const changeCurrentPath = (pathIndex) => {
    setPath(path.splice(-1, pathIndex));
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
    console.log(path);
    formData.append("path", path.join("/"));

    axios
      .post("https://localhost:8443/api/upload", formData, {
        headers: {
          Accept: "application/json",
          "Content-Type": "multipart/form-data",
        },
      })
      .then((response) => {
        let newFiles = [...files[1]];
        newFiles = newFiles.concat(response.data.files);
        newFiles.sort((a, b) => a.name.localeCompare(b.name));
        setFiles([files[0], newFiles]);
      });
  };

  const onCreateNewFolder = async (event, name) => {
    event.preventDefault();
    let formData = new FormData();
    formData.append("path", path.join("/"));
    formData.append("directoryName", name);

    axios
      .post("https://localhost:8443/api/create_directory", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      })
      .then((response) => {
        let newDirectories = [...files[0]];
        newDirectories.push(response.data.directory);
        newDirectories.sort((a, b) => a.name.localeCompare(b.name));
        setFiles([newDirectories, files[1]]);
      });
  };

  return {
    files,
    path,
    sortFileList,
    onCreateNewFolder,
    onEnterDirectory,
    onGoToParentDir,
    onFilesSubmit,
    changeCurrentPath,
  };
};

export default useListService;

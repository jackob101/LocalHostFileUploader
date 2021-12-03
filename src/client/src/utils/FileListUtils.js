import { useEffect, useState } from "react";
import axios from "axios";
import toast from "react-hot-toast";

const useListService = () => {
    const [files, setFiles] = useState([]);
    const [path, setPath] = useState([]);
    const backendUrl = process.env.REACT_APP_BACKEND_URL;

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
            .get("/api/getFiles", {
                params: {
                    path: path.join("/"),
                },
            })
            .then((response) => {
                sortFileList(response.data.files);
            });
    }, [path, backendUrl]);

    const changeCurrentPath = (pathIndex) => {
        console.log(pathIndex);
        setPath(path.slice(0, pathIndex));
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

    const concatWithoutDuplicates = (array1, array2) => {
        let newArray = array1.concat(
            array2.filter(
                (entry1) =>
                    !array1.some((entry2) => entry1.name === entry2.name)
            )
        );
        return newArray;
    };

    const onFilesSubmit = (
        event,
        uploadedFiles,
        setUploadFiles,
        overrideRef
    ) => {
        event.preventDefault();
        let formData = new FormData();
        uploadedFiles.forEach((entry) => {
            formData.append("files", entry);
        });
        formData.append("path", path.join("/"));
        formData.append("override", overrideRef.current.checked);

        axios
            .post("/api/upload", formData, {
                headers: {
                    Accept: "application/json",
                    "Content-Type": "multipart/form-data",
                },
            })
            .then((response) => {
                let newFiles = [...files[1]];
                let responseNotSaved = response.data.files.notSaved || [];
                let responseSaved = response.data.files.saved || [];

                //Need to do this to have js File object instead of my model object
                let notSaved = [];
                notSaved = uploadedFiles.filter((entry) =>
                    responseNotSaved.some(
                        (entry2) => entry2.name === entry.name
                    )
                );

                setUploadFiles(notSaved);

                newFiles = concatWithoutDuplicates(newFiles, responseSaved);

                newFiles.sort((a, b) => a.name.localeCompare(b.name));
                setFiles([files[0], newFiles]);

                if (responseNotSaved.length > 0)
                    toast.error(
                        "Not all files could be uploaded. Check if files with the same names already exists in folder you wish to upload"
                    );
            })
            .catch((error) => {
                toast.error(error.response.data);
            });
    };

    const downloadImage = (fileName) => {
        let filePath = path.concat(fileName);
        axios({
            url: "/api/download",
            responseType: "blob",
            params: {
                path: filePath.join("/"),
            },
        }).then((response) => {
            let suggestedFilename = response.headers["x-suggested-filename"];
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement("a");
            link.href = url;
            link.setAttribute("download", suggestedFilename);
            document.body.appendChild(link);
            link.click();
        });
    };

    const onCreateNewFolder = async (event, name) => {
        event.preventDefault();
        if (
            window.confirm(
                "Are you sure you want to create new folder in current directory?"
            )
        ) {
            let formData = new FormData();
            let filePath = path.join("/");
            if (filePath.length > 0) {
                filePath += "/" + name.current.value;
            } else {
                filePath += name.current.value;
            }
            formData.append("path", filePath);
            formData.append("directoryName", name.current.value);
            axios
                .post("/api/create_directory", formData, {
                    headers: { "Content-Type": "multipart/form-data" },
                })
                .then((response) => {
                    if (
                        !files[0].some(
                            (entry) =>
                                entry.name === response.data.directory.name
                        )
                    ) {
                        let newDirectories = [...files[0]];
                        newDirectories.push(response.data.directory);
                        newDirectories.sort((a, b) =>
                            a.name.localeCompare(b.name)
                        );
                        setFiles([newDirectories, files[1]]);
                        toast.success("Folder created successfully");
                    } else {
                        toast.error("Folder with this name already exists!");
                    }
                });
            name.current.value = "";
        }
    };

    const submitEdit = (path, oldName, newName) => {
        let formData = new FormData();
        formData.append("path", path);
        formData.append("oldName", oldName);
        formData.append("newName", newName);
        axios.post("/api/update", formData).then((response) => {
            //TODO this is temporary
            if (response.data.directory) {
                let newFiles = updateData(files[0], response.data, oldName);
                setFiles([newFiles, files[1]]);
            } else {
                let newFiles = updateData(files[1], response.data, oldName);
                setFiles([files[0], newFiles]);
            }
        });
    };

    const deleteFile = (filePath, name, isDirectory) => {
        let formData = new FormData();
        formData.append("path", filePath);

        axios.post("/api/delete", formData).then((response) => {
            if (response.data.deleted) {
                let newFiles;
                if (isDirectory) {
                    newFiles = [...files[0]];
                } else {
                    newFiles = [...files[1]];
                }
                newFiles = newFiles.filter((entry) => entry.name !== name);

                isDirectory
                    ? setFiles([newFiles, files[1]])
                    : setFiles([files[0], newFiles]);

                toast.success("Successfully deleted");
            } else toast.error("Error occured while deleting file/directory");
        });
    };

    const updateData = (files, data, oldName) => {
        files = files.filter((entry) => entry.name !== oldName);
        files.push(data);
        files.sort((a, b) => a.name.localeCompare(b.name));
        return files;
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
        downloadImage,
        submitEdit,
        deleteFile,
    };
};

export default useListService;

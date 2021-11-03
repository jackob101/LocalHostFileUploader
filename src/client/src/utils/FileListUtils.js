import { useEffect, useState } from "react";
import axios from "axios";
import toast from "react-hot-toast";

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
        uploadedFiles.map((entry) => {
            formData.append("files", entry);
        });
        formData.append("path", path.join("/"));
        formData.append("override", overrideRef.current.checked);
        console.log(JSON.stringify(overrideRef.current.checked));

        axios
            .post("https://localhost:8443/api/upload", formData, {
                headers: {
                    Accept: "application/json",
                    "Content-Type": "multipart/form-data",
                },
            })
            .then((response) => {
                console.log(response);
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
            });
    };

    const onCreateNewFolder = async (event, name) => {
        event.preventDefault();
        let formData = new FormData();
        formData.append("path", path.join("/"));
        formData.append("directoryName", name.current.value);

        axios
            .post("https://localhost:8443/api/create_directory", formData, {
                headers: { "Content-Type": "multipart/form-data" },
            })
            .then((response) => {
                if (
                    !files[0].some(
                        (entry) => entry.name === response.data.directory.name
                    )
                ) {
                    console.log("Creating new directory");
                    let newDirectories = [...files[0]];
                    newDirectories.push(response.data.directory);
                    newDirectories.sort((a, b) => a.name.localeCompare(b.name));
                    setFiles([newDirectories, files[1]]);
                    toast.success("Folder created successfully");
                } else {
                    toast.error("Folder with this name already exists!");
                }
            });
        name.current.value = "";
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

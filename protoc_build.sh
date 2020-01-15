ROOTDIR=`pwd`
#brew install protobuf
function build_client_js_files() {
    cd $ROOTDIR
    cd proto/blog # Some reason there is an issue when building protoc outside of the dir
    echo "Generating JS file to $ROOTDIR/client/output"
    protoc -I=. blog.proto --js_out=import_style=commonjs:../../client/output/ --grpc-web_out=import_style=commonjs,mode=grpcwebtext:../../client/output
}

function build_java_class() {
    cd $ROOTDIR
     echo "Running gradlew generateProto `./gradlew generateProto`"
}

build_client_js_files
build_java_class # Not working for some reason... gradlew not a command eh.
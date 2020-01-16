ROOTDIR=`pwd`

# Uncomment below line to install protobuf for MAC OS only...
# brew install protobuf
function build_client_js_files() {
  echo "Building Client side proto files..."
  protoc --proto_path=proto/blog --js_out=import_style=commonjs,binary:client/src/ --grpc-web_out=import_style=commonjs,mode=grpcwebtext:client/src/ proto/blog/blog.proto
}

function build_java_class() {
  echo "Running gradlew generateProto... `./gradlew generateProto`"
}

build_client_js_files
build_java_class
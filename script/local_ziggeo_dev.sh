#!/bin/bash

cd ../betajs-media-components
grunt -f
npm link
cd ../ziggeo-client-sdk
npm link betajs-media-components
npm run build
cd ../open-company-web
rm -R resources/public/lib/ziggeo/*
cp ../ziggeo-client-sdk/build/* resources/public/lib/ziggeo

#!/usr/bin/env python2

import colorsys
import json
import os
import random
import re
import shutil
import subprocess
import sys

CONFIG_PATH = 'config.json'

def main():
    types = ['separate', 'simple', 'advanced']
    argc = len(sys.argv)
    if argc == 1:
        build(types[0])
        return
    if argc == 2 and sys.argv[1] in types:
        build(sys.argv[1])
        return
    print 'Types are:', types

def build(type):
    configFile = CONFIG_PATH
    config = json.loads(open(configFile).read())

    if os.path.exists('build'):
        shutil.rmtree('build')
    os.mkdir('build')

    buildServer(config, configFile)
    buildClient(config, type)

    print '(Wipes hands.) All done!'

def buildServer(config, configFile):
    print 'Cooking the WebSocket server.'
    shutil.copytree('server', 'build/server-temp')
    generateServerFiles(config)
    os.chdir('build/server-temp')
    code = subprocess.call(['ant'])
    if code != 0:
        print 'Failed to build the server.'
        exit(1)
    os.chdir('../..')
    os.mkdir('build/server')
    shutil.copy('build/server-temp/dist/server-full.jar',
            'build/server/server.jar')
    shutil.rmtree('build/server-temp')
    shutil.copy(configFile, 'build/server/config.json')
    shutil.copytree('data', 'build/server/data')

def buildClient(config, type):
    print 'Baking the HTTP files.'

    os.mkdir('build/client')
    shutil.copy('client/style.css', 'build/client/style.css')
    shutil.copy('data/map.json', 'build/client/map.json')
    shutil.copytree('client/js', 'build/client/js')

    buildGenFile(config)

    jsFiles = ['js/' + x.encode('utf8') for x in config['jsFilesOrder']]

    func = globals()['buildClient_' + type]
    func(config, jsFiles)

    if config['copyHttpFilesTo'] != None:
        shutil.copytree('build/client', config['copyHttpFilesTo'])

def buildGenFile(config):
    GEN = {}
    for prop in config['clientPublicValues']:
        GEN[prop] = config[prop]

    file = open('build/client/js/gen.js').read() + """
        var GEN = %s;
    """ % json.dumps(GEN)

    out = open('build/client/js/gen.js', 'w')
    out.write(file)
    out.close()

def buildClient_separate(config, jsFiles):
    writeIndex(jsFiles)

def buildClient_simple(config, jsFiles):
    jsFiles = ['build/client/' + x for x in jsFiles]
    compile(jsFiles, 'build/client/main.js', 'SIMPLE_OPTIMIZATIONS')
    shutil.rmtree('build/client/js')
    writeIndex(['main.js'])

def buildClient_advanced(config, jsFiles):
    jsFiles = ['build/client/' + x for x in jsFiles]
    compile(jsFiles, 'build/client/main.js', 'ADVANCED_OPTIMIZATIONS')
    shutil.rmtree('build/client/js')
    writeIndex(['main.js'])

def writeIndex(files):
    scripts = ['<script src="%s"></script>' % f for f in files]
    index = open('client/index.html').read()
    index = re.sub(r'>\s+<', '><', index)
    index = index.replace('<!--{{SCRIPTS}}-->', '\n'.join(scripts))
    indexOut = open('build/client/index.html', 'w')
    indexOut.write(index)
    indexOut.close()
    
def compile(files, output, level=None):
    args = ['java', '-jar', 'tools/closure-compiler.jar']

    if level != None:
        args.append('--compilation_level')
        args.append(level)

    for f in files:
        args.append('--js')
        args.append(f)

    args.append('--output_wrapper')
    args.append('(function(){%output%})();')
    
    args.append('--js_output_file')
    args.append(output)

    subprocess.call(args)

def generateServerFiles(config):
    pass


if __name__ == '__main__':
    main()

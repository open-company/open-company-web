#!/usr/bin/env ruby
result = `sass scss/main.scss /tmp/built.css`
raise result unless $?.to_i == 0
raise "When compiled the module should output some CSS" unless File.exists?('built.css')
puts "Regular compile worked successfully"